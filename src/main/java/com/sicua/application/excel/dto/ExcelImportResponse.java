package com.sicua.application.excel.dto;

import java.util.List;

/**
 * Response for Excel import operation
 */
public class ExcelImportResponse {
    
    private int totalProcessed;
    private int successfulImports;
    private int categoriesCreated;
    private List<String> errors;
    private List<String> warnings;
    private String summary;
    
    // Default constructor
    public ExcelImportResponse() {}
    
    public ExcelImportResponse(int totalProcessed, int successfulImports, int categoriesCreated,
                              List<String> errors, List<String> warnings) {
        this.totalProcessed = totalProcessed;
        this.successfulImports = successfulImports;
        this.categoriesCreated = categoriesCreated;
        this.errors = errors;
        this.warnings = warnings;
        this.summary = buildSummary();
    }
    
    private String buildSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Procesado: ").append(totalProcessed).append(" productos");
        
        if (successfulImports > 0) {
            sb.append(" | Importados: ").append(successfulImports);
        }
        
        if (categoriesCreated > 0) {
            sb.append(" | Categorías creadas: ").append(categoriesCreated);
        }
        
        if (!errors.isEmpty()) {
            sb.append(" | Errores: ").append(errors.size());
        }
        
        if (!warnings.isEmpty()) {
            sb.append(" | Advertencias: ").append(warnings.size());
        }
        
        return sb.toString();
    }
    
    // Getters and setters
    public int getTotalProcessed() {
        return totalProcessed;
    }
    
    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }
    
    public int getSuccessfulImports() {
        return successfulImports;
    }
    
    public void setSuccessfulImports(int successfulImports) {
        this.successfulImports = successfulImports;
    }
    
    public int getCategoriesCreated() {
        return categoriesCreated;
    }
    
    public void setCategoriesCreated(int categoriesCreated) {
        this.categoriesCreated = categoriesCreated;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public List<String> getWarnings() {
        return warnings;
    }
    
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
}
