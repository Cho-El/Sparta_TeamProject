package com.example.intermediate.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//필수 값에 대한 조건 체크하는 것이나 DTO 에서 Domain 으로 변환하거나, Domain 에서 DTO 로 변환하는 로직은 Domain 이 아닌 DTO 에 담겨야 합니다.
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

  @NotBlank // Null, ""," " 허용하지 않음
  @Size(min = 4, max = 12) // 최소, 최대 길이를 정할 수 있음
  @Pattern(regexp = "[a-zA-Z\\d]*${3,12}")
  private String nickname;

  @NotBlank
  @Size(min = 4, max = 32)
  @Pattern(regexp = "[a-z\\d]*${3,32}")
  private String password;

  @NotBlank
  private String passwordConfirm;
}
