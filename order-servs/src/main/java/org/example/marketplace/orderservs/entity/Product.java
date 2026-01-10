package org.example.marketplace.orderservs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


public record Product(

     long id,
     String name,
     String details,
     double price
)
{}
