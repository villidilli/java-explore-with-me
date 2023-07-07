package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.debug("/create compilation");
        log.debug("Income dto: {}", compilationDto.toString());
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.debug("/delete compilation");
        log.debug("Income: compId: {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationDto) {
        log.debug("/update compilation");
        return compilationService.updateCompilation(compId, updateCompilationDto);
    }
}