import {useState} from "react";
import {useRouter} from "next/router";
import parse from "parse-link-header";
import {useAppContext} from "../context";

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

export const useLoggedParamsFiller =  () => {
    const {axios, loggedUser} = useAppContext()


    const auxFunc = async (queryParam) => {
        const likedNewsResponse = await axios.get('news', {params: {[queryParam]: loggedUser.id}})
        let likedNews = (likedNewsResponse.data || []).map(n => n.id)
        let parsedLink = await parse(likedNewsResponse.headers.get('Link'))
        while (parsedLink && parsedLink.next) {
            const nextBatchResponse = await axios.get(parsedLink.next.url)
            likedNews = likedNews.concat(nextBatchResponse.map(n => n.id))
            parsedLink = parse(nextBatchResponse.headers.get('Link'))
        }
        return likedNews
    }



    const fillLoggedParams = async (news) => {
        if (loggedUser) {
            const likedNews = await auxFunc('likedBy')

            const dislikedNews = await auxFunc('dislikedBy')

            const savedNews = await auxFunc('savedBy')
            news.forEach(n => {
                n.rating = likedNews.includes(news.id) ? 1 : (dislikedNews.includes(news.id) ? -1 : 0)
                n.saved = savedNews.includes(news.id)
            })
        }
        return news
    }

    return fillLoggedParams

}