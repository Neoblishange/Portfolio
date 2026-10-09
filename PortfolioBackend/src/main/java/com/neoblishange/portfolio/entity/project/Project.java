package com.neoblishange.portfolio.entity.project;

import com.neoblishange.portfolio.entity.ProjectImage;
import com.neoblishange.portfolio.entity.Skill;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "projects",
        uniqueConstraints = {
                @UniqueConstraint(name = "project_slug", columnNames = "slug")
        }
)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 150)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ProjectType projectType;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private List<ProjectContext> projectContext;

    @Column(nullable = false, length = 255)
    private String shortDescription;

    @Column(nullable = false)
    private List<String> description;

    @Column(nullable = false)
    private List<String> functionalities;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column()
    private LocalDate endDate;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProjectImage> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "project_skills",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public List<ProjectContext> getProjectContext() {
        return projectContext;
    }

    public void setProjectContext(List<ProjectContext> projectContext) {
        this.projectContext = projectContext;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getFunctionalities() {
        return functionalities;
    }

    public void setFunctionalities(List<String> functionalities) {
        this.functionalities = functionalities;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<ProjectImage> getImages() {
        return images;
    }

    public void setImages(List<ProjectImage> images) {
        this.images = images;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}