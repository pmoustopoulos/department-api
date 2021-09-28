package com.ainigma100.departmentapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    
    @Id
    @Column(name = "emp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(nullable = false)
    private char sex;

    @Column(nullable = false)
    private Double salary;
    
    // Many Employees are associated to one specific Department
    @ManyToOne(fetch = FetchType.LAZY,
            // we did not add the CascadeType.REMOVE because we do not want to propagate the deletion to the associated table
            // If we delete an Employee we do not want to delete a Department and vice versa
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "department_id") // Refers to PK property in 'Department' class
    private Department department;
    
}