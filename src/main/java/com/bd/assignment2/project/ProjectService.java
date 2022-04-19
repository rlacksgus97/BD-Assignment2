package com.bd.assignment2.project;

import com.bd.assignment2.config.jwt.JwtService;
import com.bd.assignment2.project.dto.CreateProjectReqDto;
import com.bd.assignment2.project.dto.ReadProjectResDto;
import com.bd.assignment2.project.dto.UpdateProjectReqDto;
import com.bd.assignment2.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final JwtService jwtService;
    private final ProjectRepository projectRepository;

    @Transactional
    public Long create(CreateProjectReqDto createProjectReqDto) {
        User user = jwtService.getUserFromJwt();
        Project project = Project.builder()
                .title(createProjectReqDto.getTitle())
                .code(createProjectReqDto.getCode())
                .build();
        user.addProject(project);
        return projectRepository.save(project).getId();
    }

    @Transactional
    public ReadProjectResDto read(Long id) {
        User user = jwtService.getUserFromJwt();
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트입니다"));
        if (project.getUser().equals(user)) {
            ReadProjectResDto readProjectResDto = ReadProjectResDto.builder()
                    .title(project.getTitle())
                    .code(project.getCode())
                    .build();
            return readProjectResDto;
        } else {
            throw new RuntimeException("프로젝트를 조회할 권한이 없습니다");
        }
    }

    @Transactional
    public Long update(Long id, String code) {
//        User user = jwtService.getUserFromJwt();
//        Project project = projectRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트입니다"));
//        if (project.getUser().equals(user)) {
//            project.update(code);
//        } else {
//            throw new RuntimeException("프로젝트를 수정할 권한이 없습니다");
//        }
//        return id;
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트입니다"));
        project.update(code);
        return id;
    }

    @Transactional
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
