package com.example.studyJWT.data;

import java.util.Set;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`user`")// h2 데이터베이스에 user는 예약어로 지정되어 있어 user명 사용 불가함
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@Column(name ="user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(name = "username", length = 50, unique = true)
	private String username;

	@Column(name = "password", length = 100)
	private String password;

	@Column(name = "nickname", length=50)
	private String nickname;

	@Column(name = "activated")
	private boolean activated;

	@ManyToMany //다대다 중간 테이블 만들기
	@JoinTable(
		name = "user_authority",
		joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
		inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
	)
	private Set<Authority> authorities;
}
