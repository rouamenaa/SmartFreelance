package com.smartfreelance.projectservice.service;

import com.smartfreelance.projectservice.entity.Project;

import java.util.List;

public interface ProjectService {

    Project createProject(Project project);

    Project approveProject(Long projectId);

    Project startProject(Long projectId);

    Project deliverProject(Long projectId);

    Project completeProject(Long projectId);

    Project cancelProject(Long projectId);

    List<Project> getAllProjects();

    Project getProjectById(Long id);

    Project updateProject(Long projectId, Project updatedProject);

    void deleteProject(Long id);

    void autoCompleteProjectIfNeeded(Long projectId);

    // 🔥 EXISTING
    double calculateProjectProgress(Long projectId);

    // 🚀 NEW — Performance Engine
    double calculateProjectPerformanceIndex(Long projectId);

    String classifyProjectPerformance(Long projectId);
}