/* (C) 2024 */ 

package com.example.demo.bookstore.lock;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DxLockInfo {
    private String txId;
    private Duration duration;
}
