package com.ainigma100.departmentapi.specification;

import com.ainigma100.departmentapi.dto.DepartmentSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Build a Specification<Department> from DepartmentSearchCriteriaDTO.
 * - departmentCode: prefix match (case-insensitive)
 * - departmentName: contains match (case-insensitive)
 * - departmentDescription: contains match (case-insensitive)
 *
 * Extend this class if you want more operators (equals, ranges, enums, etc).
 */
public final class DepartmentSpecification {

    private DepartmentSpecification() {}

    public static Specification<Department> fromCriteria(DepartmentSearchCriteriaDTO criteria) {
        return (Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria == null) {
                return cb.conjunction();
            }

            // departmentCode: prefix match (deptCode starts with value), case-insensitive
            if (StringUtils.hasText(criteria.getDepartmentCode())) {
                predicates.add(cb.like(cb.lower(root.get("departmentCode")), criteria.getDepartmentCode().toLowerCase() + "%"));
            }

            // departmentName: contains, case-insensitive
            if (StringUtils.hasText(criteria.getDepartmentName())) {
                predicates.add(cb.like(cb.lower(root.get("departmentName")), "%" + criteria.getDepartmentName().toLowerCase() + "%"));
            }

            // departmentDescription: contains, case-insensitive
            if (StringUtils.hasText(criteria.getDepartmentDescription())) {
                predicates.add(cb.like(cb.lower(root.get("departmentDescription")), "%" + criteria.getDepartmentDescription().toLowerCase() + "%"));
            }

            // Combine predicates (AND)
            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
