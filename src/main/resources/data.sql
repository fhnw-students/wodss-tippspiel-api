insert into `user` (`username`, `email`, `password`, `is_admin`)
  values  ('hirsch', 'gerhard.hirschfeld@students.fhnw.ch', '1234', 1),
          ('davu', 'david.heimgartner@students.fhnw.ch', '1234', 1),
          ('ken', 'ken.iseli@students.fhnw.ch', '1234', 0);

insert into `team` ( `id`, `name` )
  values  ( 1, 'Studies');

insert into `team_mate` ( `id`, `team_id`, `user_id`, `is_owner` )
  values  ( 1, 1, 1, 0),
          ( 2, 1, 2, 1),
          ( 3, 1, 3, 0);

insert into `tip_rule` ( `id`, `description`, `points` )
  values  ( 1, 'rule.name.winner', 10),
          ( 2, 'rule.name.home.score', 2),
          ( 3, 'rule.name.guest.score', 2),
          ( 4, 'rule.name.balance.winner', 6);

insert into `nation` ( `id`, `tournament_Group`, `code` )
  values  ( 'RUS', 'A', 'ru'),
          ( 'KSA', 'A', 'sa'),
          ( 'EGY', 'A', 'eg'),
          ( 'URU', 'A', 'uy'),
          ( 'POR', 'B', 'pt'),
          ( 'ESP', 'B', 'es'),
          ( 'MAR', 'B', 'ma'),
          ( 'IRN', 'B', 'ir'),
          ( 'FRA', 'C', 'fr'),
          ( 'AUS', 'C', 'au'),
          ( 'PER', 'C', 'pe'),
          ( 'DEN', 'C', 'dk'),
          ( 'ARG', 'D', 'ar'),
          ( 'ISL', 'D', 'is'),
          ( 'CRO', 'D', 'hr'),
          ( 'NGA', 'D', 'ng'),
          ( 'BRA', 'E', 'br'),
          ( 'SUI', 'E', 'ch'),
          ( 'CRC', 'E', 'cr'),
          ( 'SRB', 'E', 'rs'),
          ( 'GER', 'F', 'de'),
          ( 'MEX', 'F', 'mx'),
          ( 'SWE', 'F', 'se'),
          ( 'KOR', 'F', 'kr'),
          ( 'BEL', 'G', 'be'),
          ( 'PAN', 'G', 'pa'),
          ( 'TUN', 'G', 'tn'),
          ( 'ENG', 'G', 'gb-eng'),
          ( 'POL', 'H', 'pl'),
          ( 'SEN', 'H', 'sn'),
          ( 'COL', 'H', 'co'),
          ( 'JPN', 'H', 'jp');

insert into `game_phase` ( `id`, `code` )
  values  ( 1, 'phase.name.group1'),
          ( 2, 'phase.name.group2'),
          ( 3, 'phase.name.group3'),
          ( 4, 'phase.name.round16'),
          ( 5, 'phase.name.quarters'),
          ( 6, 'phase.name.semis'),
          ( 7, 'phase.name.third'),
          ( 8, 'phase.name.finals');

insert into `location` ( `id`, `code`, `utc_diff` )
values  ( 1, 'location.name.Moscow.Luzhniki', 3),
        ( 2, 'location.name.Yekaterinburg.Central', 5),
        ( 3, 'location.name.SaintPetersburg.Krestovsky', 3),
        ( 4, 'location.name.Rostov-on-Don.Rostov', 3),
        ( 5, 'location.name.Samara.Cosmos', 4),
        ( 6, 'location.name.Volgograd.Volgograd', 3),
        ( 7, 'location.name.Sochi.Fisht', 3),
        ( 8, 'location.name.Kazan.Kazan', 3),
        ( 9, 'location.name.Saransk.Mordovia', 3),
        ( 10, 'location.name.Kaliningrad.Kaliningrad', 2),
        ( 11, 'location.name.Moscow.Otkrytiye', 3),
        ( 12, 'location.name.NizhnyNovgorod.Nizhny', 3);

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
        ( 10, '2018-06-17 16:00:00', 'CRC', 'SRB', 1, 5),
        ( 9,  '2018-06-17 21:00:00', 'BRA', 'SUI', 1, 4),
        ( 25, '2018-06-22 15:00:00', 'BRA', 'CRC', 2, 3),
        ( 26, '2018-06-22 20:00:00', 'SRB', 'SUI', 2, 10),
        ( 41, '2018-06-27 21:00:00', 'SRB', 'BRA', 3, 11),
        ( 42, '2018-06-27 21:00:00', 'SUI', 'CRC', 3, 12),
        ( 11, '2018-06-17 18:00:00', 'GER', 'MEX', 1, 1),
        ( 12, '2018-06-18 15:00:00', 'SWE', 'KOR', 1, 12),
        ( 28, '2018-06-23 18:00:00', 'KOR', 'MEX', 2, 4),
        ( 27, '2018-06-23 21:00:00', 'GER', 'SWE', 2, 7),
        ( 43, '2018-06-27 17:00:00', 'KOR', 'GER', 3, 8),
        ( 44, '2018-06-27 19:00:00', 'MEX', 'SWE', 3, 2),
        ( 13, '2018-06-18 18:00:00', 'BEL', 'PAN', 1, 7),
        ( 14, '2018-06-18 21:00:00', 'TUN', 'ENG', 1, 6),
        ( 29, '2018-06-23 15:00:00', 'BEL', 'TUN', 2, 11),
        ( 30, '2018-06-24 15:00:00', 'ENG', 'PAN', 2, 12),
        ( 45, '2018-06-28 20:00:00', 'ENG', 'BEL', 3, 10),
        ( 46, '2018-06-28 21:00:00', 'PAN', 'TUN', 3, 9),
        ( 16, '2018-06-19 15:00:00', 'COL', 'JPN', 1, 9),
        ( 15, '2018-06-19 18:00:00', 'POL', 'SEN', 1, 11),
        ( 32, '2018-06-24 20:00:00', 'JPN', 'SEN', 2, 2),
        ( 31, '2018-06-24 21:00:00', 'POL', 'COL', 2, 8),
        ( 47, '2018-06-28 17:00:00', 'JPN', 'POL', 3, 6),
        ( 48, '2018-06-28 18:00:00', 'SEN', 'COL', 3, 5);