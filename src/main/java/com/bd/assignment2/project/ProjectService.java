package com.bd.assignment2.project;

import com.bd.assignment2.config.jwt.JwtService;
import com.bd.assignment2.project.dto.CreateProjectReqDto;
import com.bd.assignment2.project.dto.ReadProjectResDto;
import com.bd.assignment2.project.dto.UpdateProjectReqDto;
import com.bd.assignment2.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final JwtService jwtService;
    private final ProjectRepository projectRepository;

    public Long create(CreateProjectReqDto createProjectReqDto) {
        User user = jwtService.getUserFromJwt();
        Project project = Project.builder()
                .title(createProjectReqDto.getTitle())
                .code(createProjectReqDto.getCode())
                .build();
        user.addProject(project);
        return projectRepository.save(project).getId();
    }

    public ReadProjectResDto read(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트입니다"));
        ReadProjectResDto readProjectResDto = ReadProjectResDto.builder()
                .title(project.getTitle())
                .code(project.getCode())
                .build();
        return readProjectResDto;
    }

    public Long update(Long id, UpdateProjectReqDto updateProjectReqDto) {
        User user = jwtService.getUserFromJwt();
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트입니다"));
        if (project.getUser().equals(user)) {
            project.update(updateProjectReqDto);
        } else {
            throw new RuntimeException("프로젝트를 수정할 권한이 없습니다");
        }
        return id;
    }

    public Long delete(Long id) {
        User user = jwtService.getUserFromJwt();
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트입니다"));
        if (project.getUser().equals(user)) {
            projectRepository.delete(project);
        } else {
            throw new RuntimeException("프로젝트를 삭제할 권한이 없습니다");
        }
        return id;
    }
}
