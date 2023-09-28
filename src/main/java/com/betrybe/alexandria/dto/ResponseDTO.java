package com.betrybe.alexandria.dto;

public record ResponseDTO<T>(String message, T data) {

}
