package by.l0gik67.jdbc.dto;


import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String email;
}
