package com.carla.backend_exercise.dto;

import lombok.Data;

@Data
public class CompensationDto {
  private String jobTitle;
  private String industry;
  private String location;
  private Long salary;
  private String currency;
}
