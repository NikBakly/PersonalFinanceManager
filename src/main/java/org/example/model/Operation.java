package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.util.OperationCategory;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Operation implements Serializable {
    private final String category;
    private final double amount;
    private final OperationCategory type;

}