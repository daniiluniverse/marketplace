const API_BASE_URL = 'http://localhost:8088';

// Загрузить корзину
async function loadCart() {
    const userId = document.getElementById('userId').value;

    try {
        const response = await fetch(`${API_BASE_URL}/${userId}/cart`);

        if (response.status === 404) {
            clearCartDisplay();
            return;
        }

        if (!response.ok) throw new Error('Ошибка загрузки корзины');

        const cart = await response.json();
        displayCart(cart);

    } catch (error) {
        console.error('Error:', error);
        alert('Ошибка при загрузке корзины');
    }
}

// Отобразить корзину
function displayCart(cart) {
    const cartItems = document.getElementById('cartItems');
    const itemCount = document.getElementById('itemCount');
    const total = document.getElementById('total');

    if (!cart.items || cart.items.length === 0) {
        cartItems.innerHTML = `
            <div class="empty-cart">
                <i class="fas fa-cart-plus"></i>
                <p>Корзина пуста</p>
            </div>
        `;
        itemCount.textContent = '0';
        total.textContent = '0';
        return;
    }

    // Обновить счетчики
    itemCount.textContent = cart.itemCount;
    total.textContent = formatPrice(cart.total);

    // Отобразить товары
    cartItems.innerHTML = cart.items.map(item => `
        <div class="cart-item" data-product-id="${item.productId}">
            <div class="item-info">
                <div class="item-name">${item.productName}</div>
                <div class="item-id">ID товара: ${item.productId}</div>
                <div class="item-price">${formatPrice(item.price)} ₽</div>
            </div>
            
            <div class="item-quantity">
                <div class="quantity-controls">
                    <button onclick="updateQuantity(${item.productId}, -1)">-</button>
                    <span id="quantity-${item.productId}">${item.quantity}</span>
                    <button onclick="updateQuantity(${item.productId}, 1)">+</button>
                </div>
                <div class="item-subtotal">${formatPrice(item.subtotal)} ₽</div>
            </div>
            
            <button class="remove-btn" onclick="removeItem(${item.productId})">
                <i class="fas fa-trash"></i> Удалить
            </button>
        </div>
    `).join('');
}

// Очистить отображение корзины
function clearCartDisplay() {
    document.getElementById('cartItems').innerHTML = `
        <div class="empty-cart">
            <i class="fas fa-cart-plus"></i>
            <p>Корзина пуста</p>
        </div>
    `;
    document.getElementById('itemCount').textContent = '0';
    document.getElementById('total').textContent = '0';
}

// Добавить товар в корзину
async function addToCart() {
    const userId = document.getElementById('userId').value;
    const productId = document.getElementById('productId').value;
    const productName = document.getElementById('productName').value;
    const price = document.getElementById('price').value;
    const quantity = document.getElementById('quantity').value;

    if (!productId || !productName || !price) {
        alert('Пожалуйста, заполните все поля');
        return;
    }

    const request = {
        productId: parseInt(productId),
        productName: productName,
        price: parseFloat(price),
        quantity: parseInt(quantity)
    };

    try {
        const response = await fetch(`${API_BASE_URL}/${userId}/cart/items`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        });

        if (!response.ok) throw new Error('Ошибка добавления товара');

        const cart = await response.json();
        displayCart(cart);

        // Очистить форму
        document.getElementById('productId').value = '';
        document.getElementById('productName').value = '';
        document.getElementById('price').value = '';
        document.getElementById('quantity').value = '1';

    } catch (error) {
        console.error('Error:', error);
        alert('Ошибка при добавлении товара');
    }
}

// Обновить количество товара
async function updateQuantity(productId, delta) {
    const userId = document.getElementById('userId').value;
    const currentElement = document.getElementById(`quantity-${productId}`);
    const currentQuantity = parseInt(currentElement.textContent);
    const newQuantity = Math.max(1, currentQuantity + delta);

    try {
        const response = await fetch(
            `${API_BASE_URL}/${userId}/cart/items/${productId}?quantity=${newQuantity}`,
            { method: 'PUT' }
        );

        if (!response.ok) throw new Error('Ошибка обновления количества');

        const cart = await response.json();
        displayCart(cart);

    } catch (error) {
        console.error('Error:', error);
        alert('Ошибка при обновлении количества');
    }
}

// Удалить товар из корзины
async function removeItem(productId) {
    const userId = document.getElementById('userId').value;

    if (!confirm('Удалить товар из корзины?')) return;

    try {
        const response = await fetch(
            `${API_BASE_URL}/${userId}/cart/items/${productId}`,
            { method: 'DELETE' }
        );

        if (!response.ok) throw new Error('Ошибка удаления товара');

        loadCart();

    } catch (error) {
        console.error('Error:', error);
        alert('Ошибка при удалении товара');
    }
}

// Очистить всю корзину
async function clearCart() {
    const userId = document.getElementById('userId').value;

    if (!confirm('Очистить всю корзину?')) return;

    try {
        const response = await fetch(
            `${API_BASE_URL}/${userId}/cart`,
            { method: 'DELETE' }
        );

        if (!response.ok) throw new Error('Ошибка очистки корзины');

        clearCartDisplay();

    } catch (error) {
        console.error('Error:', error);
        alert('Ошибка при очистке корзины');
    }
}

// Оформить заказ
async function checkout() {
    const userId = document.getElementById('userId').value;
    const address = prompt('Введите адрес доставки:', 'ул. Примерная, д. 1, кв. 1');

    if (!address) {
        alert('Адрес доставки обязателен');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/${userId}/cart/checkout`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ address: address })
        });

        if (!response.ok) throw new Error('Ошибка оформления заказа');

        const order = await response.json();
        displayOrder(order);

        // Очистить корзину после оформления
        clearCartDisplay();

    } catch (error) {
        console.error('Error:', error);
        alert('Ошибка при оформлении заказа');
    }
}

// Отобразить детали заказа
function displayOrder(order) {
    const ordersSection = document.getElementById('ordersSection');
    const orderDetails = document.getElementById('orderDetails');

    ordersSection.style.display = 'block';

    orderDetails.innerHTML = `
        <div><strong>ID заказа:</strong> ${order.id}</div>
        <div><strong>Статус:</strong> <span style="color: #28a745">${order.status || 'ОБРАБОТАН'}</span></div>
        <div><strong>Общая сумма:</strong> ${formatPrice(order.totalAmount)} ₽</div>
        <div><strong>Адрес доставки:</strong> ${order.shippingAddress}</div>
        <div><strong>Дата создания:</strong> ${new Date(order.createdAt).toLocaleString()}</div>
        ${order.items ? `
            <div style="margin-top: 15px;">
                <strong>Товары:</strong>
                <ul style="margin-top: 10px;">
                    ${order.items.map(item => `
                        <li>${item.productName} - ${item.quantity} шт. × ${formatPrice(item.price)} ₽</li>
                    `).join('')}
                </ul>
            </div>
        ` : ''}
    `;
}

// Форматирование цены
function formatPrice(price) {
    return new Intl.NumberFormat('ru-RU', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(price);
}

// Загрузить корзину при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    loadCart();

    // Разрешить нажатие Enter в полях ввода
    const inputs = ['productId', 'productName', 'price', 'quantity'];
    inputs.forEach(id => {
        document.getElementById(id).addEventListener('keypress', (e) => {
            if (e.key === 'Enter') addToCart();
        });
    });
});