package co.com.bar.bar_app.domain.model;

public record PaginationRequest(
        int page,
        int size,
        String sortBy,
        String direction
) {
}
