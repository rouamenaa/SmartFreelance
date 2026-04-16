INSERT INTO skill_market (skill, demand_count, freelancer_count, avg_budget)
VALUES ('React', 180, 60, 1200.00)
ON DUPLICATE KEY UPDATE
    demand_count = VALUES(demand_count),
    freelancer_count = VALUES(freelancer_count),
    avg_budget = VALUES(avg_budget);

INSERT INTO skill_market (skill, demand_count, freelancer_count, avg_budget)
VALUES ('Spring Boot', 150, 70, 1400.00)
ON DUPLICATE KEY UPDATE
    demand_count = VALUES(demand_count),
    freelancer_count = VALUES(freelancer_count),
    avg_budget = VALUES(avg_budget);

INSERT INTO skill_market (skill, demand_count, freelancer_count, avg_budget)
VALUES ('Angular', 130, 90, 1100.00)
ON DUPLICATE KEY UPDATE
    demand_count = VALUES(demand_count),
    freelancer_count = VALUES(freelancer_count),
    avg_budget = VALUES(avg_budget);

INSERT INTO skill_market (skill, demand_count, freelancer_count, avg_budget)
VALUES ('DevOps', 120, 40, 1800.00)
ON DUPLICATE KEY UPDATE
    demand_count = VALUES(demand_count),
    freelancer_count = VALUES(freelancer_count),
    avg_budget = VALUES(avg_budget);

INSERT INTO skill_market (skill, demand_count, freelancer_count, avg_budget)
VALUES ('Node.js', 160, 100, 1000.00)
ON DUPLICATE KEY UPDATE
    demand_count = VALUES(demand_count),
    freelancer_count = VALUES(freelancer_count),
    avg_budget = VALUES(avg_budget);
