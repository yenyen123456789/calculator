package com.calculator.demo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(
            @RequestParam BigDecimal num1,
            @RequestParam BigDecimal num2,
            @RequestParam String operator) {

        BigDecimal result;

        try {
            switch (operator) {
                case "add":
                    result = num1.add(num2);
                    break;

                case "subtract":
                    result = num1.subtract(num2);
                    break;

                case "multiply":
                    result = num1.multiply(num2);
                    break;

                case "divide":
                    if (num2.compareTo(BigDecimal.ZERO) == 0) {
                        return ResponseEntity.badRequest().body(
                            Map.of("error", "Cannot divide by zero.")
                        );
                    }

                    result = num1.divide(
                        num2, 10, RoundingMode.HALF_UP
                    ).stripTrailingZeros();
                    break;

                default:
                    return ResponseEntity.badRequest().body(
                        Map.of("error", "Invalid operator.")
                    );
            }

            return ResponseEntity.ok(
                Map.of("result", result.toPlainString())
            );

        } catch (ArithmeticException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Unable to calculate this result.")
            );
        }
    }
}

