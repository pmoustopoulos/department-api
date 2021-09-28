package com.ainigma100.departmentapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "department")
public class Department {
    
    @Id
    @Column(name = "dep_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer depId;

    @Column(name = "dep_name", nullable = false)
    private String depName;

    // A Department can have many Employees
    @JsonIgnore
    @OneToMany(mappedBy = "department", // Refers to 'department' property in 'Employee' class
                // we did not add the CascadeType.REMOVE because we do not want to propagate the deletion to the associated table
                // If we delete a Department we do not want to delete an Employee and vice versa
                cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                        CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Employee> employees = new HashSet<>();


    // add convenience method  for bi-directional relationship
    public void addEmployee(Employee tempEmployee) {

        // this will set up a Bi-directional link between the Department and an Employee
        employees.add(tempEmployee);

        tempEmployee.setDepartment(this);
    }
}
