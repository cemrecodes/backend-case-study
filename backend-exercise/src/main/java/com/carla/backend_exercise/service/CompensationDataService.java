package com.carla.backend_exercise.service;

import com.carla.backend_exercise.dto.CompensationDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

@Service
public class CompensationDataService {

  public List<CompensationDto> getCompensationData(Long minSalary, Long maxsalary, String currency, String location, String industry, String jobTitle, String sortBy) {
    List<CompensationDto> filteredData = new ArrayList<>();

    ClassPathResource resource = new ClassPathResource("data/salary_survey-1.json");
    try (InputStream inputStream = resource.getInputStream()) {
      byte[] buffer = new byte[inputStream.available()];
      inputStream.read(buffer);
      String jsonData = new String(buffer);

      JSONArray jsonArray = new JSONArray(jsonData);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String salaryStr = jsonObject.optString("What is your annual salary?", "").replaceAll("[^\\d]", "");
        String currencyStr = jsonObject.optString("Please indicate the currency", "");


        if (!salaryStr.isEmpty()) {
          Long annualSalary = Long.parseLong(salaryStr);

          boolean matchesFilters = (maxsalary == null || annualSalary <= maxsalary) && (minSalary == null || annualSalary >= minSalary) &&
              (currency == null || currencyStr.equals(currency)) &&
              (location == null || jsonObject.optString("Where are you located? (City/state/country)", "").toLowerCase().contains(location.toLowerCase())) &&
              (industry == null || jsonObject.optString("What industry do you work in?", "").equals(industry)) &&
              (jobTitle == null || jsonObject.optString("Job title", "").toLowerCase().contains(jobTitle.toLowerCase()));

          if (matchesFilters) {
            CompensationDto dto = new CompensationDto();
            dto.setJobTitle(jsonObject.optString("Job title", ""));
            dto.setIndustry(jsonObject.optString("What industry do you work in?", ""));
            dto.setLocation(jsonObject.optString("Where are you located? (City/state/country)", ""));
            dto.setSalary(annualSalary);
            dto.setCurrency(jsonObject.optString("Please indicate the currency", ""));

            filteredData.add(dto);
          }
        }
      }

      if (sortBy != null) {
        filteredData = sortCompensationData(sortBy, filteredData);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return filteredData;
  }

  public JSONObject getCompensationDataByIndex(int index, String fields) {
    ClassPathResource resource = new ClassPathResource("data/salary_survey-1.json");
    try (InputStream inputStream = resource.getInputStream()) {
      byte[] buffer = new byte[inputStream.available()];
      inputStream.read(buffer);
      String jsonData = new String(buffer);

      JSONArray jsonArray = new JSONArray(jsonData);
      if (index >= 0 && index < jsonArray.length()) {
        JSONObject jsonObject = jsonArray.getJSONObject(index);
        return sparseJson(jsonObject, fields);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  private List<CompensationDto> sortCompensationData(String sortBy, List<CompensationDto> filteredData) {
    List<Comparator<CompensationDto>> comparators = Arrays.stream(sortBy.split(","))
        .map(String::trim)
        .map(sortField -> {
          switch (sortField.toLowerCase()) {
            case "salary":
              return Comparator.comparing(CompensationDto::getSalary);
            case "jobtitle":
              return Comparator.comparing(CompensationDto::getJobTitle);
            case "location":
              return Comparator.comparing(CompensationDto::getLocation);
            case "industry":
              return Comparator.comparing(CompensationDto::getIndustry);
            default:
              return null;
          }
        })
        .filter(java.util.Objects::nonNull)
        .toList();

    Comparator<CompensationDto> finalComparator = comparators.stream()
        .reduce(Comparator::thenComparing)
        .orElse((c1, c2) -> 0);

    filteredData.sort(finalComparator);
    return filteredData;
  }

  private JSONObject sparseJson(JSONObject jsonObject, String fields) {
    JSONObject newJson = new JSONObject();

    if (fields == null || fields.isEmpty()) {
      fields = "salary,jobtitle,location,industry,currency";
    }

    JSONObject filteredJson = new JSONObject(jsonObject.toString());
    String[] requestedFields = fields.split(",");
    for (String field : requestedFields) {
      if (field.equalsIgnoreCase("salary")) {
        String salaryStr = jsonObject.optString("What is your annual salary?", "").replaceAll("[^\\d]", "");
        Long annualSalary = null;
        if (!salaryStr.isEmpty()) {
           annualSalary= Long.parseLong(salaryStr);
        }
        newJson.put("salary", annualSalary);
      }
      else if (field.equalsIgnoreCase("jobtitle")) {
        newJson.put("jobtitle", filteredJson.get("Job title"));
      }
      else if (field.equalsIgnoreCase("location")) {
        newJson.put("location", filteredJson.get("Where are you located? (City/state/country)"));
      }
      else if (field.equalsIgnoreCase("industry")) {
        newJson.put("industry", filteredJson.get("What industry do you work in?"));
      }
      else if(field.equalsIgnoreCase("currency")) {
        newJson.put("currency", filteredJson.get("Please indicate the currency"));
      }
    }
    return newJson;
  }
}
