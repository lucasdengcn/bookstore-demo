package com.example.demo.bookstore.lock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DxLockInfo {
    private String txId;
    private Duration duration;
}
