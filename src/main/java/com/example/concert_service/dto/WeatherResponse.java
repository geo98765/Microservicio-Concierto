package com.example.concert_service.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

    private String location;
    private String date;
    private String weather;
    private String temperature;
    private String precipitation;
    private String humidity;
    private String wind;

    @SerializedName("answer_box")
    private AnswerBox answerBox;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerBox {
        private String location;
        private String date;
        private String weather;
        private String temperature;
        private String precipitation;
        private String humidity;
        private String wind;

        @SerializedName("thumbnail")
        private String thumbnail;

        @SerializedName("forecast")
        private List<Forecast> forecast;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Forecast {
        private String day;
        private String weather;
        private String temperature;

        @SerializedName("high_temp")
        private String highTemp;

        @SerializedName("low_temp")
        private String lowTemp;

        private String precipitation;
        private String humidity;
        private String wind;
        private String thumbnail;
    }
}
