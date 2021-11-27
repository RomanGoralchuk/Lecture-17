CREATE TABLE `people` (
	`people_id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` CHAR(50) NOT NULL,
	`surname` CHAR(50) NULL DEFAULT NULL,
	`pet_id` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`people_id`) USING BTREE,
	INDEX `FK_people_pet` (`pet_id`) USING BTREE,
	CONSTRAINT `FK_people_pet`
	FOREIGN KEY (`pet_id`)
	REFERENCES `hibernateTest`.`pet` (`pet_id`)
	ON UPDATE CASCADE ON DELETE SET NULL
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;