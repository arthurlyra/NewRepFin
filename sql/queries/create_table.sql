create table republicas
(
	republica_id int not null auto_increment,
    name varchar(255) not null,
    primary key (republica_id)
);


create table users
(
	user_id int not null auto_increment,
    name varchar(255) not null,
    password varchar(255) not null,
    republica_id int,
    balance varchar(255) not null,
    exist boolean not null,
    primary key (user_id),
    foreign key (republica_id) references republicas(republica_id)
);


create table transactions
(
	transaction_id int not null auto_increment,
    description varchar(255) not null,
    total_value float not null,
    responsible_user_id int not null,
    republica_id int not null,
    primary key (transaction_id),
    foreign key (republica_id) references republicas(republica_id),
    foreign key (responsible_user_id) references users(user_id)
);

create table users_transactions
(
	transaction_id int not null,
    user_id int not null,
    value float not null,
    foreign key (transaction_id) references transactions(transaction_id),
    foreign key (user_id) references users(user_id)
)