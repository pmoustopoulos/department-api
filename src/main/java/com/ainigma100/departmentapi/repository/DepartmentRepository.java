package com.ainigma100.departmentapi.repository;

import com.ainigma100.departmentapi.dto.DepartmentSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // << add this
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    Department findByDepartmentCode(String departmentCode);


    @Query(value = """
            select dep from Department dep
            where ( :#{#criteria.departmentCode} IS NULL OR LOWER(dep.departmentCode) LIKE LOWER(CONCAT(:#{#criteria.departmentCode}, '%')) )
            and ( :#{#criteria.departmentName} IS NULL OR LOWER(dep.departmentName) LIKE LOWER(CONCAT(:#{#criteria.departmentName}, '%')) )
            and ( :#{#criteria.departmentDescription} IS NULL OR LOWER(dep.departmentDescription) LIKE LOWER(CONCAT('%', :#{#criteria.departmentDescription}, '%')) )
            """)
    Page<Department> getAllDepartmentsUsingPagination(
            @Param("criteria") DepartmentSearchCriteriaDTO departmentSearchCriteriaDTO,
            Pageable pageable);

}
