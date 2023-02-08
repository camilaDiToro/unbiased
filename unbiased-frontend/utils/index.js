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


    const auxFunc = async (entity, queryParam, accept) => {
        try {
            const likedNewsResponse = await axios.get(entity, {params: {[queryParam]: loggedUser.id}, headers: {
                'Accept': accept
                }}, {authOptional: true})
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
            return new Promise((resolveInner) => {
                return []
            })
        }

    }

    const fillNewsLoggedParams = async (news) => {
        console.log(news)
        if (loggedUser) {
            const likedNewsPromise = auxFunc('news','likedBy')
            const dislikedNewsPromise = auxFunc('news','dislikedBy')

            const savedNewsPromise = auxFunc('news','savedBy')

            const reportedNewsPromise = auxFunc('news','reportedBy')

            const [likedNews, dislikedNews, savedNews, reportedNews] = await Promise.all([likedNewsPromise, dislikedNewsPromise, savedNewsPromise, reportedNewsPromise])
            console.log('reported')
            console.log(reportedNews)

            news.forEach(n => {
                n.rating = likedNews.includes(n.id) ? 1 : (dislikedNews.includes(n.id) ? -1 : 0)
                n.saved = savedNews.includes(n.id)

                n.reported = reportedNews.includes(n.id)
            })
            console.log(news)
        }
        return news
    }

    const fillCommentsLoggedParams = async (comments) => {
        if (loggedUser) {
            const likedCommentsPromise =  auxFunc('comments','likedBy')
            const dislikedCommentsPromise =  auxFunc('comments','dislikedBy')
            const reportedCommentsPromise = auxFunc('comments','reportedBy')
            const [likedComments, dislikedComments, reportedComments] = await Promise.all([likedCommentsPromise, dislikedCommentsPromise, reportedCommentsPromise])

            console.log('reported coments')
            console.log(reportedComments)
            comments.forEach(n => {
                n.rating = likedComments.includes(n.id) ? 1 : (dislikedComments.includes(n.id) ? -1 : 0)
                n.reported = reportedComments.includes(n.id)
            })
        }
        return comments
    }

    return {fillNewsLoggedParams, fillCommentsLoggedParams}

}