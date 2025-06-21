package com.test.library.main.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BookSearchRequestDto implements BasePagingRequestDto {
    @PositiveOrZero
    private Integer page;
    @Positive
    private Integer pagesize;
    private String title;
    private String author;
    private Boolean isUnreturned;
    private LocalDate checkOutStart;
    private LocalDate checkOutEnd;
    private LocalDate expectedReturnStart;
    private LocalDate expectedReturnEnd;
    private LocalDate returnStart;
    private LocalDate returnEnd;
}
