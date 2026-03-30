package lk.slife.policyreportexporter.entity.mariadb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "master_persondata")
public class MasterPersonDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "pin")
    private String pin;

    @Column(name = "personname")
    private String personname;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "occupation", length = 500)
    private String occupation;

    @Column(name = "gender", length = 55)
    private String gender;

    @Column(name = "proposal_no")
    private String proposalNo;

    @Column(name = "fir_ins")
    private String firIns;

    @Column(name = "next_ins")
    private String nextIns;

    @Column(name = "nic_no", length = 100)
    private String nicNo;

    @Column(name = "age")
    private Integer age;

    @Column(name = "citizenship", length = 100)
    private String citizenship;

    @Column(name = "civil_status", length = 50)
    private String civilStatus;

    @Column(name = "residence_tel_no", length = 100)
    private String residenceTelNo;

    @Column(name = "mobile_no", length = 100)
    private String mobileNo;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "postal_code", length = 100)
    private String postalCode;

    @Column(name = "employment_location", length = 100)
    private String employmentLocation;

    @Column(name = "nature_business")
    private String natureBusiness;

    @Column(name = "monthly_income", length = 122)
    private String monthlyIncome;

    @Column(name = "business_startdate", length = 200)
    private String businessStartdate;

    @Column(name = "business_reg_no", length = 100)
    private String businessRegNo;

    @Column(name = "debts_status", length = 100)
    private String debtsStatus;

    @Column(name = "outstanding_capital", length = 100)
    private String outstandingCapital;

    @Column(name = "monthly_installment", length = 100)
    private String monthlyInstallment;

    @Column(name = "residence_tel_no2")
    private String residenceTelNo2;

    @Column(name = "status", length = 100)
    private String status;


}