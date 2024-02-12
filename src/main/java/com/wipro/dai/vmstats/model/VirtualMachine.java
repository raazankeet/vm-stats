package com.wipro.dai.vmstats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "zerotouch_virtual_machines")
public class VirtualMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer VMID;

    private String VMName;
    private String cloudProvider;
    private String region;
    private String OS;

    @Column(name = "ProvisioningDate")
    private LocalDateTime provisioningDate;

    @Column(name = "LastUpdate")
    private LocalDateTime lastUpdate;

}

