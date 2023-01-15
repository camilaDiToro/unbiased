import {useState} from "react";
import {useRouter} from "next/router";

export const useTriggerEffect = () => {
    const [effectTrigger, setEffectTrigger] = useState(false)
    const triggerEffect = () => {
        setEffectTrigger(t => !t)
    }
    return [effectTrigger, triggerEffect]
}

export const useURLWithParams = () => {
    const router = useRouter()
    return (url, exclude) => {

        for (const [key, value] of Object.entries(router.query)) {
            if (!exclude.includes(key)) {
                url.searchParams.set(key, value)
            }
        }
    }
}