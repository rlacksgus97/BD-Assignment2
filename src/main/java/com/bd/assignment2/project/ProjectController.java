package com.bd.assignment2.project;

import com.bd.assignment2.project.dto.CreateProjectReqDto;
import com.bd.assignment2.project.dto.ReadProjectResDto;
import com.bd.assignment2.project.dto.UpdateProjectReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Long> create(CreateProjectReqDto createProjectReqDto) {
        return ResponseEntity.ok(projectService.create(createProjectReqDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadProjectResDto> read(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.read(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, UpdateProjectReqDto updateProjectReqDto) {
        return ResponseEntity.ok(projectService.update(id, updateProjectReqDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.delete(id));
    }
}
