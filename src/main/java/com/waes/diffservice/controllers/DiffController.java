package com.waes.diffservice.controllers;

import com.waes.diffservice.dto.Difference;
import com.waes.diffservice.enitities.DiffData;
import com.waes.diffservice.services.DiffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diff")
@RequiredArgsConstructor
public class DiffController {

    private final DiffService diffService;

    @PutMapping(value = "/{diffId}/left", consumes = MediaType.TEXT_PLAIN_VALUE)
    public DiffData leftText(@PathVariable Long diffId, @RequestBody String leftText) {
        return diffService.saveLeftJson(diffId, leftText);
    }

    @PutMapping(value = "/{diffId}/right", consumes = MediaType.TEXT_PLAIN_VALUE)
    public DiffData rightText(@PathVariable Long diffId, @RequestBody String rightText) {
        return diffService.saveRightJson(diffId, rightText);
    }

    @GetMapping(value = "/{diffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Difference diff(@PathVariable Long diffId) {
        return diffService.diff(diffId);
    }
}
