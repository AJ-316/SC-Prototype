package gameplayHook.SimUIPackage;

import com.formdev.flatlaf.ui.FlatLineBorder;
import gameplayHook.MachinePackage.Machine;
import gameplayHook.MachinePackage.components.MachineContext;
import gameplayHook.SimUIPackage.Panels.MachinesPanel;
import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MachineSelectButton extends JButton {

        private static final SelectMachineOnClick selectMachineOnClick = new SelectMachineOnClick();
        private final MachineContext machineContext;
        
        public MachineSelectButton(MachineContext machineContext) {
            super(machineContext.getMachineName());
            this.machineContext = machineContext;
            addActionListener(selectMachineOnClick);
        }
        
        private static class SelectMachineOnClick implements ActionListener {

            private static MachineSelectButton selectedButton;
            private static final GradientBorder selectedBorder = new GradientBorder(
                    new Insets(4, 4, 4, 4),
                    new Color(55, 196, 114),
                    new Color(55, 196, 182),
                    new Color(21, 131, 209), 4, 16
            );
            private static final FlatLineBorder deselectedBorder = new FlatLineBorder(
                    new Insets(4, 4, 4, 4),
                    new Color(1, 1, 1, 0.1f), 1, 16
            );

            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedButton != null) {
                    selectedButton.setEnabled(true);
                    selectedButton.getParent().setBackground(null);
                    ((JPanel)selectedButton.getParent()).setBorder(deselectedBorder);
                }

                selectedButton = (MachineSelectButton) e.getSource();
                selectedButton.setEnabled(false);
                ((JPanel)selectedButton.getParent()).setBorder(selectedBorder);

                SimEventsHandler.triggerEvent(SimEventsHandler.EVENT_ON_SELECT_MACHINE, selectedButton.machineContext);
            }
        }
    }