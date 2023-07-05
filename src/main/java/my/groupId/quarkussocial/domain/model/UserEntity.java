package my.groupId.quarkussocial.domain.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "UserEntity")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

}
