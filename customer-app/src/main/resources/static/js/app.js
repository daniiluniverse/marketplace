const API_BASE_URL = 'http://localhost:8085/customer/products';

// DOM элементы
const productListEl = document.getElementById('productList');
const favouritesListEl = document.getElementById('favouritesList');

// Загрузка данных при старте
document.addEventListener('DOMContentLoaded', () => {
    loadProducts();
    loadFavourites();
});

// Получение токена из localStorage (пример)
function getAuthToken() {
    return localStorage.getItem('authToken'); // Или из кук, если используете
}

// Загрузка списка продуктов
async function loadProducts() {
    try {
        const response = await fetch(`${API_BASE_URL}/list`, {
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`
            }
        });

        if (response.status === 403) {
            alert('Пожалуйста, войдите в систему');
            window.location.href = '/login';
            return;
        }

        const products = await response.json();
        productListEl.innerHTML = '';
        products.forEach(product => {
            renderProduct(product, productListEl);
        });
    } catch (error) {
        console.error('Ошибка загрузки продуктов:', error);
    }
}

// Загрузка избранных продуктов
async function loadFavourites() {
    try {
        const response = await fetch(`${API_BASE_URL}/favourite`, {
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`
            }
        });

        if (response.status === 403) {
            return; // Не показываем ошибку, так как loadProducts уже перенаправит
        }

        const favourites = await response.json();
        favouritesListEl.innerHTML = '';

        if (favourites.length === 0) {
            favouritesListEl.innerHTML = '<p>У вас пока нет избранных товаров</p>';
        } else {
            favourites.forEach(product => {
                renderFavouriteProduct(product, favouritesListEl);
            });
        }
    } catch (error) {
        console.error('Ошибка загрузки избранных:', error);
    }
}

// Отображение продукта (без изменений)
function renderProduct(product, container) {
    const productEl = document.createElement('div');
    productEl.className = 'product-card';
    productEl.innerHTML = `
        <div class="product-name">${product.name}</div>
        <div class="product-details">${product.details}</div>
        <div class="product-price">$${product.price}</div>
        <button onclick="toggleFavourite(${product.id}, this)">В избранное</button>
        <button onclick="showReviews(${product.id}, this)">Отзывы</button>
    `;
    container.appendChild(productEl);
}

// Отображение избранного продукта (без изменений)
function renderFavouriteProduct(product, container) {
    // Проверяем наличие productId вместо id
    if (!product?.productId) {
        console.error('Ошибка: у товара отсутствует productId', product);
        return;
    }

    const productEl = document.createElement('div');
    productEl.className = 'product-card';
    productEl.innerHTML = `
        <div class="product-name">${product.name}</div>
        <div class="product-details">${product.details}</div>
        <div class="product-price">$${product.price}</div>
        <button class="remove" 
                onclick="removeFromFavourites(${product.productId}, this)">
            Удалить из избранного ✓
        </button>
    `;
    container.appendChild(productEl);
}

async function removeFromFavourites(productId, button) {
    try {
        // 1. Проверка ID
        if (!productId || isNaN(productId)) {
            throw new Error(`Неверный ID товара: ${productId}`);
        }

        // 2. Формирование URL
        const url = `${API_BASE_URL}/${productId}/favourite-product/delete/`;
        console.log('Отправка запроса на:', url);

        // 3. Отправка запроса
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`,
                'Content-Type': 'application/json'
            }
        });

        // 4. Обработка ответа
        if (response.status === 404) {
            throw new Error('Товар не найден в избранном');
        }

        if (response.status === 403) {
            window.location.href = '/login';
            return;
        }

        if (!response.ok) {
            const error = await response.json().catch(() => ({}));
            throw new Error(error.message || 'Ошибка сервера');
        }

        // 5. Обновление UI
        if (button) {
            button.textContent = 'В избранное';
            button.disabled = true;
        }
        loadFavourites();

    } catch (error) {
        console.error('Ошибка удаления:', {
            message: error.message,
            stack: error.stack
        });
        alert(`Ошибка: ${error.message}`);
    }
}

// Добавление/удаление из избранного
async function toggleFavourite(productId, button) {
    try {
        const isCurrentlyFavourite = button.textContent.includes('✓');
        const url = `${API_BASE_URL}/${productId}/favourite-product/${isCurrentlyFavourite ? 'delete' : 'add'}/`;
        const method = isCurrentlyFavourite ? 'DELETE' : 'POST';

        const response = await fetch(url, {
            method,
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.status === 403) {
            alert('Войдите в систему для изменения избранного');
            window.location.href = '/login';
            return;
        }

        if (!response.ok) throw new Error('Ошибка запроса');

        // Обновляем UI
        button.textContent = isCurrentlyFavourite ? 'В избранное' : 'В избранном ✓';
        loadFavourites();
    } catch (error) {
        console.error('Ошибка переключения избранного:', error);
        alert('Не удалось изменить избранное');
    }
}

// Показать отзывы (с авторизацией)
async function showReviews(productId, button) {
    const productCard = button.closest('.product-card');
    let reviewsSection = productCard.querySelector('.reviews-section');

    if (reviewsSection) {
        reviewsSection.remove();
        return;
    }

    reviewsSection = document.createElement('div');
    reviewsSection.className = 'reviews-section';
    reviewsSection.innerHTML = `
        <div class="review-form">
            <h3>Добавить отзыв</h3>
            <textarea id="reviewText-${productId}" placeholder="Ваш отзыв"></textarea>
            <input type="number" id="reviewRating-${productId}" min="1" max="5" placeholder="Рейтинг (1-5)">
            <button onclick="addReview(${productId})">Отправить</button>
        </div>
        <div class="reviews-list" id="reviewsList-${productId}">Загрузка отзывов...</div>
    `;

    productCard.appendChild(reviewsSection);

    try {
        const response = await fetch(`${API_BASE_URL}/${productId}/get-reviews`, {
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`
            }
        });

        if (response.status === 403) {
            document.getElementById(`reviewsList-${productId}`).innerHTML =
                '<p>Войдите в систему для просмотра отзывов</p>';
            return;
        }

        // Проверяем, есть ли содержимое в ответе
        const responseText = await response.text();
        let reviews = [];

        if (responseText) {
            try {
                reviews = JSON.parse(responseText);
            } catch (e) {
                console.error('Ошибка парсинга отзывов:', e);
            }
        }

        const reviewsList = document.getElementById(`reviewsList-${productId}`);
        reviewsList.innerHTML = '';

        if (!reviews || reviews.length === 0) {
            reviewsList.innerHTML = '<p>Пока нет отзывов</p>';
        } else {
            reviews.forEach(review => {
                const reviewEl = document.createElement('div');
                reviewEl.className = 'review-item';
                reviewEl.innerHTML = `
                    <div class="rating">Рейтинг: ${'★'.repeat(review.rating)}</div>
                    <div>${review.review}</div>
                `;
                reviewsList.appendChild(reviewEl);
            });
        }
    } catch (error) {
        console.error('Ошибка загрузки отзывов:', error);
        document.getElementById(`reviewsList-${productId}`).innerHTML =
            '<p>Ошибка загрузки отзывов</p>';
    }
}

// Добавление отзыва
async function addReview(productId) {
    const reviewText = document.getElementById(`reviewText-${productId}`).value;
    const reviewRating = document.getElementById(`reviewRating-${productId}`).value;

    if (!reviewText || !reviewRating) {
        alert('Заполните все поля');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/${productId}/create-review`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                review: reviewText,
                rating: parseInt(reviewRating)
            })
        });

        if (response.status === 403) {
            alert('Войдите в систему для добавления отзыва');
            return;
        }

        if (!response.ok) throw new Error('Ошибка сервера');

        // Обновляем отзывы и очищаем форму
        showReviews(productId);
        document.getElementById(`reviewText-${productId}`).value = '';
        document.getElementById(`reviewRating-${productId}`).value = '';
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось добавить отзыв');
    }
}