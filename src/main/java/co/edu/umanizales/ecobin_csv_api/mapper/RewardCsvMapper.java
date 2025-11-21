package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.Reward;

public class RewardCsvMapper {

    public Reward fromCsv(String[] columns) {
        long id = Long.parseLong(columns[0]);
        String name = columns[1];
        long costPoints = Long.parseLong(columns[2]);
        int stock = Integer.parseInt(columns[3]);
        String description = columns.length > 4 ? columns[4] : "";

        Reward reward = new Reward();
        reward.setId(id);
        reward.setName(name);
        reward.setCostPoints(costPoints);
        reward.setStock(stock);
        reward.setDescription(description);

        return reward;
    }

    public String[] toCsv(Reward reward) {
        return new String[] {
                String.valueOf(reward.getId()),
                reward.getName(),
                String.valueOf(reward.getCostPoints()),
                String.valueOf(reward.getStock()),
                reward.getDescription() == null ? "" : reward.getDescription()
        };
    }
}
