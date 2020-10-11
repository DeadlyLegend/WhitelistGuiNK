package com.deadlylegend.whitelistgui;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import com.denzelcode.form.FormAPI;
import com.denzelcode.form.element.Button;
import com.denzelcode.form.element.Input;
import com.denzelcode.form.event.PlayerCustomFormSubmit;
import com.denzelcode.form.event.PlayerSimpleFormButtonClick;
import com.denzelcode.form.window.CustomWindowForm;
import com.denzelcode.form.window.SimpleWindowForm;

import java.io.IOException;

public class Main extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "whitelistgui":
                FormAPI.simpleWindowForm("wlg", "§6Whitelist Gui")
                        .addButton("enable", "§2> Enable Whitelist <")
                        .addButton("disable", "§c> Disable Whitelist <")
                        .addButton("add", "§2> Add a player to the whitelist <")
                        .addButton("remove", "§4> Remove a player from the whitelist <")
                        .sendTo((Player) sender);
        break;
        }
        return true;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMainClick(PlayerSimpleFormButtonClick event){
        Player player = event.getPlayer();
        Button button = event.getButton();

        if(!event.isFormValid("wlg")) return;

        if (button.getName().equalsIgnoreCase("enable")){
            this.getServer().setPropertyBoolean("white-list", true);
            player.sendMessage("§a[WLG] Enabled the Whitelist");
        }

        if (button.getName().equalsIgnoreCase("disable")){
            this.getServer().setPropertyBoolean("white-list", false);
            player.sendMessage("§c[WLG] Disabled the Whitelist");
        }

        if (button.getName().equalsIgnoreCase("add")){
            sendAddGui(player);
        }

        if (button.getName().equalsIgnoreCase("remove")){
            sendRemoveGui(player);
        }
    }

    public void sendAddGui(Player player){
        FormAPI.customWindowForm("addGui", "§6Add Player")
                .addInput("addTarget", "Player name", "Enter player you want to add")
                .sendTo(player);
    }

    public void sendRemoveGui(Player player){
        FormAPI.customWindowForm("removeGui", "§6Remove Player")
                .addInput("removeTarget", "Player name", "Enter player you want to remove")
                .sendTo(player);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAddFormSubmit(PlayerCustomFormSubmit event){
        CustomWindowForm form = event.getForm();
        Player player = event.getPlayer();

        if(!event.isFormValid("addGui")) return;

        Input addTarget = form.getElement("addTarget");

        player.sendMessage("§a[WLG] Added " + addTarget.getValue() + " §ato the Whitelist");

        this.getServer().addWhitelist(addTarget.getValue());

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRemoveFormSubmit(PlayerCustomFormSubmit e){
        CustomWindowForm form = e.getForm();
        Player player = e.getPlayer();

        if(!e.isFormValid("removeGui")) return;

        Input removeTarget = form.getElement("removeTarget");

        player.sendMessage("§c[WLG] Removed " + removeTarget.getValue() + " §cfrom the Whitelist");

        this.getServer().removeWhitelist(removeTarget.getValue());
    }

}
