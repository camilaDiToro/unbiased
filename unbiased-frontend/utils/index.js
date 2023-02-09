import {useState} from "react";


export const useTriggerEffect = () => {
    const [effectTrigger, setEffectTrigger] = useState(false)
    const triggerEffect = () => {
        setEffectTrigger(t => !t)
    }
    return [effectTrigger, triggerEffect]
}

