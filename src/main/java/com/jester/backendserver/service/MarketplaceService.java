package com.jester.backendserver.service;

import com.jester.backendserver.model.Marketplace;
import com.jester.backendserver.repository.MarketplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MarketplaceService {

    private final MarketplaceRepository marketplaceRepository;

    @Autowired
    public MarketplaceService(MarketplaceRepository marketplaceRepository) {
        this.marketplaceRepository = marketplaceRepository;
    }

    /**
     * Adds a persona to the marketplace for sale by a specific user.
     *
     * @param username   the username of the seller
     * @param personaName the name of the persona to be sold
     */
    public void sellPersona(String username, String personaName) {
        marketplaceRepository.sellPersona(username, personaName);
    }

    /**
     * Allows a user to buy a persona from the marketplace.
     *
     * @param username   the username of the buyer
     * @param personaName the name of the persona to be bought
     */
    public void buyPersona(String username, String personaName) {
        marketplaceRepository.buyPersona(username, personaName);
    }

    /**
     * Removes a persona from the marketplace for a specific user.
     *
     * @param username   the username of the seller
     * @param personaName the name of the persona to be removed
     */
    public void removeFromMarketplace(String username, String personaName) {
        marketplaceRepository.removeFromMarketplace(username, personaName);
    }

    /**
     * Retrieves the list of personas available in the marketplace for a specific user.
     *
     * @param username the username of the user browsing the marketplace
     * @return a list of personas in the marketplace
     */
    public List<Marketplace> getMarketplace(String username) {
        return marketplaceRepository.getMarketplace(username);
    }

    /**
     * Retrieves the list of personas sold by a specific seller.
     *
     * @param username the username of the seller
     * @return a list of personas sold by the seller
     */
    public List<Marketplace> getSellerPersonas(String username) {
        return marketplaceRepository.getSellerPersonas(username);
    }
}
