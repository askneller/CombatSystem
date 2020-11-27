/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.combatSystem.behaviors.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.terasology.behaviors.components.TargetComponent;
import org.terasology.combatSystem.weaponFeatures.components.PrimaryAttackComponent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.logic.behavior.BehaviorAction;
import org.terasology.logic.behavior.core.Actor;
import org.terasology.logic.behavior.core.BaseAction;
import org.terasology.logic.behavior.core.BehaviorState;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;

/**
 * Send a {@code PrimaryAttackEvent} to the entity so that it attacks its current target.
 */
@BehaviorAction(name = "primary_attack")
public class PrimaryAttackAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(PrimaryAttackAction.class);

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        TargetComponent targetComponent = actor.getComponent(TargetComponent.class);

        if (targetComponent.target == null) {
            logger.error("No target component");
            return BehaviorState.FAILURE;
        }
        LocationComponent targetLocation = targetComponent.target.getComponent(LocationComponent.class);
        if (targetLocation == null) {
            logger.error("No target location");
            return BehaviorState.FAILURE;
        }
        PrimaryAttackComponent primaryAttackComponent = actor.getComponent(PrimaryAttackComponent.class);
        if (primaryAttackComponent == null) {
            logger.error("No primary attack component");
            return BehaviorState.FAILURE;
        }

        LocationComponent locationComponent = actor.getComponent(LocationComponent.class);
        Vector3f instigatorPosition = new Vector3f(locationComponent.getWorldPosition());
        Vector3f direction = new Vector3f();
        direction.sub(targetLocation.getWorldPosition(), instigatorPosition);
        PrimaryAttackEvent event =
                new PrimaryAttackEvent(actor.getEntity(), targetComponent.target, instigatorPosition, direction);

        actor.getEntity().send(event);

        return BehaviorState.SUCCESS;
    }

}
