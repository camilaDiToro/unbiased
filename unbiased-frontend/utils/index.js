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


    const auxFunc = async (entity, queryParam) => {
        try {
            const likedNewsResponse = await axios.get(entity, {params: {[queryParam]: loggedUser.id}}, {authOptional: true})
            let likedNews = (likedNewsResponse.data || []).map(n => n.id)
            let parsedLink = await parse(likedNewsResponse.headers.get('Link'))
            while (parsedLink && parsedLink.next) {
                const nextBatchResponse = await axios.get(parsedLink.next.url)
                const data = nextBatchResponse.data
                likedNews = likedNews.concat(data.map(n => n.id))
                parsedLink = parse(nextBatchResponse.headers.get('Link'))
            }
            return likedNews
        } catch(e) {
            console.log(e)
            return []
        }

    }

    const fillNewsLoggedParams = async (news) => {
        console.log(news)
        if (loggedUser) {
            const likedNews = await auxFunc('news','likedBy')
            const dislikedNews = await auxFunc('news','dislikedBy')

            const savedNews = await auxFunc('news','savedBy')
            console.log(savedNews)

            news.forEach(n => {
                n.rating = likedNews.includes(n.id) ? 1 : (dislikedNews.includes(n.id) ? -1 : 0)
                n.saved = savedNews.includes(n.id)
            })
        }
        return news
    }

    const fillCommentsLoggedParams = async (comments) => {
        if (loggedUser) {
            const likedComments = await auxFunc('comments','likedBy')
            const dislikedComments = await auxFunc('comments','dislikedBy')


            comments.forEach(n => {
                n.rating = likedComments.includes(n.id) ? 1 : (likedComments.includes(n.id) ? -1 : 0)
            })
        }
        return comments
    }

    return {fillNewsLoggedParams, fillCommentsLoggedParams}

}