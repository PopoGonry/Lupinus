package com.popogonry.lupinus.region;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Region {
    public String name;
    public World world;
    public List<Integer> position1;
    public List<Integer> position2;
    public Region(String name, World world, List<Integer> position1, List<Integer> position2) {
        this.name = name;
        this.world = world;
        this.position1 = position1;
        this.position2 = position2;
    }
    public Location returnLocation() {
        return new Location(this.world, ((position1.get(0) + position2.get(0))/2), ((position1.get(1) + position2.get(1))/2), ((position1.get(2) + position2.get(2))/2));
    }
}
