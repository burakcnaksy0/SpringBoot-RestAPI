package b.aksoy.ILService.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Il {
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	
	@Id
	@Column(name = "id")
	private String id;	//@GeneratedValue(strategy = GenerationType.IDENTITY) // Otomatik artan primary key
	
	@NotNull
	@Column(name = "name")
	private String name;
}
