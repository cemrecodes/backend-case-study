package com.carla.backend_exercise.controller;

import com.carla.backend_exercise.dto.CompensationDto;
import com.carla.backend_exercise.service.CompensationDataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compensation_data")
@RequiredArgsConstructor
public class CompensationDataController {

  private final CompensationDataService compensationDataService;

  @GetMapping
  public ResponseEntity<List<CompensationDto>> getCompensationData(
      @RequestParam(required = false) Long min_salary,
      @RequestParam(required = false) Long max_salary,
      @RequestParam(required = false) String currency,
      @RequestParam(required = false) String location,
      @RequestParam(required = false) String industry,
      @RequestParam(required = false) String job_title,
      @RequestParam(required = false) String sortBy
  ) {
    return ResponseEntity.ok(
        compensationDataService.getCompensationData(min_salary, max_salary, currency, location, industry, job_title, sortBy)
    );
  }

  @GetMapping("/{index}")
  public ResponseEntity<String> getCompensationDataById(
      @PathVariable("index") int index,
      @RequestParam(required = false) String fields
  ) {
    JSONObject jsonObject = compensationDataService.getCompensationDataByIndex(index, fields);
    if (!jsonObject.isEmpty()) {
      return ResponseEntity.ok(jsonObject.toString());
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}