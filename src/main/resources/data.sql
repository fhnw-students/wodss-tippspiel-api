insert into `user` (`username`, `email`, `password`, `is_admin`)
  values  ('hirsch', 'gerhard.hirschfeld@students.fhnw.ch', '1234', 1),
          ('davu', 'david.heimgartner@students.fhnw.ch', '1234', 1),
          ('ken', 'ken.iseli@students.fhnw.ch', '1234', 0);


insert into `nation` ( `id`, `name`, `tournament_Group` )
  values  ( 'RUS', 'Russia', 'A' ),
          ( 'KSA', 'Saudi Arabia', 'A' ),
          ( 'EGY', 'Egypt', 'A' ),
          ( 'URU', 'Uruguay', 'A' ),
          ( 'POR', 'Portugal', 'B' ),
          ( 'ESP', 'Spain', 'B' ),
          ( 'MAR', 'Morocco', 'B' ),
          ( 'IRN', 'Iran', 'B' ),
          ( 'FRA', 'France', 'C' ),
          ( 'AUS', 'Australia', 'C' ),
          ( 'PER', 'Peru', 'C' ),
          ( 'DEN', 'Denmark', 'C' ),
          ( 'ARG', 'Argentina', 'D' ),
          ( 'ISL', 'Iceland', 'D' ),
          ( 'CRO', 'Croatia', 'D' ),
          ( 'NGA', 'Nigeria', 'D' ),
          ( 'BRA', 'Brazil', 'E' ),
          ( 'SUI', 'Switzerland', 'E' ),
          ( 'CRC', 'Costa Rica', 'E' ),
          ( 'SRB', 'Serbia', 'E' ),
          ( 'GER', 'Germany', 'F' ),
          ( 'MEX', 'Mexico', 'F' ),
          ( 'SWE', 'Sweden', 'F' ),
          ( 'KOR', 'South Korea', 'F' ),
          ( 'BEL', 'Belgium', 'G' ),
          ( 'PAN', 'Panama', 'G' ),
          ( 'TUN', 'Tunisia', 'G' ),
          ( 'ENG', 'England', 'G' ),
          ( 'POL', 'Poland', 'H' ),
          ( 'SEN', 'Senegal', 'H' ),
          ( 'COL', 'Colombia', 'H' ),
          ( 'JPN', 'Japan', 'H' );

insert into `game_phase` ( `id`, `name` )
  values  ( 1, 'Groupphase 1' ),
          ( 2, 'Groupphase 2' ),
          ( 3, 'Groupphase 3' ),
          ( 4, 'Round of 16' ),
          ( 5, 'Quarter-finals' ),
          ( 6, 'Semi-finals' ),
          ( 7, 'Match for third place' ),
          ( 8, 'Final' );

insert into `location` ( `id`, `name`, `utc_diff` )
values  ( 1, 'Luzhniki Stadium, Moscow', 3),
        ( 2, 'Central Stadium, Yekaterinburg', 5),
        ( 3, 'Krestovsky Stadium, Saint Petersburg', 3),
        ( 4, 'Rostov Arena, Rostov-on-Don', 3),
        ( 5, 'Cosmos Arena, Samara', 4),
        ( 6, 'Volgograd Arena, Volgograd', 3),
        ( 7, 'Fisht Olympic Stadium, Sochi', 3),
        ( 8, 'Kazan Arena, Kazan', 3),
        ( 9, 'Mordovia Arena, Saransk', 3),
        ( 10, 'Kaliningrad Stadium, Kaliningrad', 2),
        ( 11, 'Otkrytiye Arena, Moscow', 3),
        ( 12, 'Nizhny Novgorod Stadium, Nizhny Novgorod', 3);

insert into `game` ( `id`, `date`, `host_nation_id`, `guest_nation_id`, `game_phase_id`, `location_id` )
values  ( 1,  '2018-06-14 18:00:00', 'RUS', 'KSA', 1, 1),
        ( 2,  '2018-06-15 17:00:00', 'EGY', 'URU', 1, 2),
        ( 17, '2018-06-19 21:00:00', 'RUS', 'EGY', 2, 3),
        ( 18, '2018-06-20 18:00:00', 'URU', 'KSA', 2, 4),
        ( 33, '2018-06-25 18:00:00', 'URU', 'RUS', 3, 5),
        ( 34, '2018-06-25 17:00:00', 'KSA', 'EGY', 3, 6),
        ( 4,  '2018-06-15 18:00:00', 'MAR', 'IRN', 1, 3),
        ( 3,  '2018-06-15 21:00:00', 'POR', 'ESP', 1, 7),
        ( 19, '2018-06-20 15:00:00', 'POR', 'MAR', 2, 1),
        ( 20, '2018-06-20 21:00:00', 'IRN', 'ESP', 2, 8),
        ( 35, '2018-06-25 21:00:00', 'IRN', 'POR', 3, 9),
        ( 36, '2018-06-25 20:00:00', 'ESP', 'MAR', 3, 10),
        ( 5,  '2018-06-16 13:00:00', 'FRA', 'AUS', 1, 8),
        ( 6,  '2018-06-16 19:00:00', 'PER', 'DEN', 1, 9),
        ( 22, '2018-06-21 16:00:00', 'DEN', 'AUS', 2, 5),
        ( 21, '2018-06-21 20:00:00', 'FRA', 'PER', 2, 2),
        ( 37, '2018-06-26 17:00:00', 'DEN', 'FRA', 3, 1),
        ( 38, '2018-06-26 17:00:00', 'AUS', 'PER', 3, 7),
        ( 7,  '2018-06-16 16:00:00', 'ARG', 'ISL', 1, 11),
        ( 8,  '2018-06-16 21:00:00', 'CRO', 'NGA', 1, 10),
        ( 23, '2018-06-21 21:00:00', 'ARG', 'CRO', 2, 12),
        ( 24, '2018-06-22 18:00:00', 'NGA', 'ISL', 2, 6),
        ( 39, '2018-06-26 21:00:00', 'NGA', 'ARG', 3, 3),
        ( 40, '2018-06-26 21:00:00', 'ISL', 'CRO', 3, 4),
        ( 10, '2018-06-17 16:00:00', 'COR', 'SRB', 1, 5),
        ( 9,  '2018-06-17 21:00:00', 'BRA', 'SUI', 1, 4),
        ( 25, '2018-06-22 15:00:00', 'BRA', 'COR', 2, 3),
        ( 26, '2018-06-22 20:00:00', 'SRB', 'SUI', 2, 10),
        ( 41, '2018-06-27 21:00:00', 'SRB', 'BRA', 3, 11),
        ( 42, '2018-06-27 21:00:00', 'SUI', 'COR', 3, 12),
        ( 11, '2018-06-17 18:00:00', 'GER', 'MEX', 1, 1),
        ( 12, '2018-06-18 15:00:00', 'SWE', 'KOR', 1, 12),
        ( 28, '2018-06-23 18:00:00', 'KOR', 'MEX', 2, 4),
        ( 27, '2018-06-23 21:00:00', 'GER', 'SWE', 2, 7),
        ( 43, '2018-06-27 17:00:00', 'KOR', 'GER', 3, 8),
        ( 44, '2018-06-27 19:00:00', 'MEX', 'SWE', 3, 2),
        ( 13, '2018-06-18 18:00:00', 'BEL', 'PAM', 1, 7),
        ( 14, '2018-06-18 21:00:00', 'TUN', 'ENG', 1, 6),
        ( 29, '2018-06-23 15:00:00', 'BEL', 'TUN', 2, 11),
        ( 30, '2018-06-24 15:00:00', 'ENG', 'PAM', 2, 12),
        ( 45, '2018-06-28 20:00:00', 'ENG', 'BEL', 3, 10),
        ( 46, '2018-06-28 21:00:00', 'PAM', 'TUN', 3, 9),
        ( 16, '2018-06-19 15:00:00', 'COL', 'JPN', 1, 9),
        ( 15, '2018-06-19 18:00:00', 'POL', 'SEN', 1, 11),
        ( 32, '2018-06-24 20:00:00', 'JPN', 'SEN', 2, 2),
        ( 31, '2018-06-24 21:00:00', 'POL', 'COL', 2, 8),
        ( 47, '2018-06-28 17:00:00', 'JPN', 'POL', 3, 6),
        ( 48, '2018-06-28 18:00:00', 'SEN', 'COL', 3, 5);