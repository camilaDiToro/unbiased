import {commentsMapper, newsMapper, userMapper} from "../mappers"
import parse from "parse-link-header";
import {useRouter} from "next/router";
import FormData from "form-data";
import {useSnackbar} from "notistack";

const log = true

export const useApi = (loggedUser, axios) => {
    const { enqueueSnackbar, closeSnackbar } = useSnackbar();
    const router = useRouter()
    return new Api(loggedUser, axios, router, enqueueSnackbar)
}

export class Api {
    constructor(loggedUser, axios, router, enqueueSnackbar) {
        this.axios = axios
        this.loggedUser = loggedUser
        this.router = router
        this.enqueueSnackbar = enqueueSnackbar
    }

    async #getPagination(res) {

            if (!res.headers.get('Link')) {
                return {currentPage: 1, lastPage: 1}
            }
            let maybeCurrent = parseInt(this.router.query.page || '1')
            const parsedLink = parse(res.headers.get('Link'))
            const last = parseInt(parsedLink.last.page)
            if (maybeCurrent > last) {
                maybeCurrent = last
            } else if (maybeCurrent < 1) {
                maybeCurrent = 1
            }
            return {currentPage: maybeCurrent, lastPage: last}

    }

    async login(email, password) {
        const action = async () => {
            try {
                const res = await this.axios({
                    method: 'put',
                    url: 'users/0/pinnedNews',
                    auth: {username: email, password},
                    params: {newsId: 0},
                    hideError: true
                })
            }
            catch(error) {
                if (!error.response || !(error.response.status === 404)) {
                    throw new Error("Error logging in")
                }
            }


        }
        return await Api.#runRequest(action)

    }

    async getArticles(params, hideError, authOptional) {
        const action = async () => {
            const res = await this.#getArticlesCall(params, hideError, authOptional, params)
            if (!res || !res.success)
                throw new Error(res.error)
            const filledNews = await this.#fillNewsParams(res.data)
            return {data: filledNews, pagination: res.pagination}
        }

        return await Api.#runRequest(action)
    }

    async #getArticlesCall(params, hideError, authOptional) {
        const action = async () => {
            const res = await this.axios.get('news', {params}, undefined, {authOptional: authOptional, hideError: hideError})
            const mappedNews = (res.data || []).map(newsMapper)
            return {data: mappedNews, pagination: await this.#getPagination(res)}
        }

        return await Api.#runRequest(action, params)
    }

    async #getCommentsCall(params, hideError, authOptional) {
        const action = async () => {
            const res = await this.axios.get('comments', {params}, undefined, {authOptional: authOptional, hideError: hideError})
            const mappedNews = (res.data || []).map(commentsMapper)
            return {data: mappedNews, pagination: await this.#getPagination(res)}
        }

        return await Api.#runRequest(action)
    }

    static async #getAllIdsFrom(apiCall, params) {
        const r = await apiCall(params, true, true)
        if (!r.success)
            return []
        let {currentPage, lastPage} = r.pagination
        let array = r.data.map(n => n.id)
        while(currentPage < lastPage) {
            const {data, success, pagination} = await apiCall({...params, page: currentPage + 1}, true, true)
            if (!success) {
                return array
            }
            currentPage++
            lastPage = pagination.lastPage
            array = array.concat(data.map(n => n.id))
        }
        return array
    }

    async #fillNewsParams(articles) {
        // return articles
        if (this.loggedUser) {
            const id = this.loggedUser.id
            const instanceMethodCall = (...a) => this.#getArticlesCall(...a)
            const likedNewsPromise = Api.#getAllIdsFrom(instanceMethodCall,{likedBy: id})
            const pinnedNewsPromise = Api.#getAllIdsFrom(instanceMethodCall,{pinnedBy: id})

            const dislikedNewsPromise = Api.#getAllIdsFrom(instanceMethodCall,{dislikedBy: id})

            const savedNewsPromise = Api.#getAllIdsFrom(instanceMethodCall,{savedBy: id})

            const reportedNewsPromise = Api.#getAllIdsFrom(instanceMethodCall,{reportedBy: id})

            const [likedNews, dislikedNews, savedNews, reportedNews, pinnedNews] = await Promise.all([likedNewsPromise, dislikedNewsPromise, savedNewsPromise, reportedNewsPromise, pinnedNewsPromise])

            articles.forEach(n => {
                n.rating = likedNews.includes(n.id) ? 1 : (dislikedNews.includes(n.id) ? -1 : 0)
                n.saved = savedNews.includes(n.id)
                n.pinned = pinnedNews.includes(n.id)
                n.reported = reportedNews.includes(n.id)
            })
            return articles
        }

        return articles
    }

    async #fillCommentsParams(comments) {
        if (this.loggedUser) {
            const id = this.loggedUser.id
            const instanceMethodCall = (...a) => this.#getCommentsCall(...a)
            const likedCommentsPromise = Api.#getAllIdsFrom(instanceMethodCall,{likedBy: id})

            const dislikedCommentsPromise = Api.#getAllIdsFrom(instanceMethodCall,{dislikedBy: id})


            const reportedCommentsPromise = Api.#getAllIdsFrom(instanceMethodCall,{reportedBy: id})

            const [likedComments, dislikedComments, reportedComments] = await Promise.all([likedCommentsPromise, dislikedCommentsPromise,  reportedCommentsPromise])

            comments.forEach(n => {
                n.rating = likedComments.includes(n.id) ? 1 : (dislikedComments.includes(n.id) ? -1 : 0)
                n.reported = reportedComments.includes(n.id)
            })
            return comments
        }

        return comments
    }

    static async #runRequest(func, optional) {
        try {

            const data = await func()
            const obj = typeof data === 'object' && data.hasOwnProperty('data') ? data : {data}
            const toReturn =  {...obj, success: true, optional}
            if (log)
                console.log(toReturn)
            return toReturn
        }
        catch(e) {
            const toReturn =  {success: false, error: e, optional}
            if (log)
                console.log(toReturn)
            return toReturn
        }
    }

    async postComment(comment, newsId) {
        const stringifiedJson = JSON.stringify({comment})
        const action = async () => {
            await this.axios.post('comments', stringifiedJson, {
                headers: {
                    'Content-Type': 'application/vnd.unbiased.comment.v1+json',
                },
                params: {newsId}
            })
        }
        return await Api.#runRequest(action)
    }

    async postArticle(article, image) {
        const action = async () => {
            const res = await this.axios.post('news', JSON.stringify(article),{
                headers: {
                    'Content-Type': 'application/json',
                }
            })

            const splittedLocation = res.headers.location.split('/')
            const id = splittedLocation[splittedLocation.length - 1]
            if (image) {
                const f =new FormData()
                f.set('image', image)
                await this.axios.put(`news/${id}/image`, f)
            }

            return {id}
        }

        return await Api.#runRequest(action)
    }



    async deleteArticle(newsId) {
        const action = async () => {
            await this.axios.delete(`news/${newsId}`)
        }

        return await Api.#runRequest(action)
    }

    async addBookmark(newsId) {
        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.put(`/news/${newsId}/bookmarks/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async deleteBookmark(newsId) {
        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.delete(`/news/${newsId}/bookmarks/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async addPin(newsId) {
        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.put(`/users/${id}/pinnedNews`,null, {params: {newsId}})
        }

        return await Api.#runRequest(action)
    }

    async deletePin() {
        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.delete(`/users/${id}/pinnedNews`)
        }

        return await Api.#runRequest(action)
    }

    async registerUser(email, password) {
        const action = async () => {
            await this.axios.post('users',JSON.stringify({
                email,
                password
            }),{
                headers: {
                    'Content-Type': 'application/json',
                }
            })
        }

        return await Api.#runRequest(action)
    }

    async addRole(userId, role) {
        const action = async () => {
            const res = await this.axios.put(`users/${userId}/role`,undefined, {
                params: {role}
            })
        }

        return await Api.#runRequest(action)
    }

    async removeRole(userId, role) {
        const action = async () => {
            const res = await this.axios.delete(`users/${userId}/role`, {
                params: {role}
            })
        }

        return await Api.#runRequest(action)
    }

    async getComments(params, authOptional, hideError) {
        const action = async () => {
            const res = await this.#getCommentsCall(params, hideError, authOptional)
            if (!res.success)
                throw new Error(res.error)
            const mappedData = await this.#fillCommentsParams(res.data)
            return {data: mappedData, pagination: res.pagination}
        }

        return await Api.#runRequest(action)
    }

    async getUsers(params) {

        const getUsersData = async res => {
            const data = res.data || []

            const finalData = data.map(d => {
                const aux = d
                delete aux['newsStats']
                return aux
            })
            return {data: finalData ? finalData.map(userMapper) : [], pagination: await this.#getPagination(res)}
        }


        const action = async () => {
            return await this.axios.get('users', {params}).then(getUsersData)
        }

        return await Api.#runRequest(action)
    }

    async getUser(id) {
        const action = async () => {
            const relativePath = `users/${id}`
            const res = await this.axios.get(relativePath, undefined, {
                headers: {
                    'Content-Type': "application/vnd.unbiased.user.v1+json"
                }
            })
            const data = res.data
            let userInfo
            if (data && data.newsStats) {
                const stats = await this.axios.get(data.newsStats)
                data.newsStats = stats.data
                userInfo = userMapper(res.data)
            } else {
                userInfo = res.data ? userMapper(res.data) : {}
            }
            return userInfo
        }

        return await Api.#runRequest(action)
    }

    async getFollowing(userId) {
        const action = async () => {
            const res = await this.axios.get(`users/${userId}/following`)
            const following = res.data || []
            return following
        }

        return await Api.#runRequest(action)
    }

    async reportComment(commentId, reason) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.put(`comments/${commentId}/reports/${id}`, JSON.stringify({reason}),{
                headers: {
                    'Content-Type': 'application/vnd.unbiased.commentReportDetail.v1+json',
                }
            })
        }

        return await Api.#runRequest(action)
    }

    async reportArticle(newsId, reason) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.put(`news/${newsId}/reports/${id}`, JSON.stringify({reason}),{
                headers: {
                    'Content-Type': 'application/json',
                }
            })
        }

        return await Api.#runRequest(action)
    }

    async upvoteComment(commentId) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.put(`comments/${commentId}/likes/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async upvoteArticle(newsId) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.put(`news/${newsId}/likes/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async downvoteComment(commentId) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.put(`comments/${commentId}/dislikes/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async downvoteArticle(newsId) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.put(`news/${newsId}/dislikes/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async downvoteCommentRemove(commentId) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.delete(`comments/${commentId}/dislikes/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async downvoteArticleRemove(newsId) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.delete(`news/${newsId}/dislikes/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async upvoteCommentRemove(commentId) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.delete(`comments/${commentId}/likes/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async upvoteArticleRemove(newsId) {

        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            await this.axios.delete(`news/${newsId}/likes/${id}`)
        }

        return await Api.#runRequest(action)
    }

    async deleteComment(newsId) {
        const action = async () => {
            await this.axios.delete(`comments/${newsId}`)
        }

        return await Api.#runRequest(action)
    }



    async getArticle(newsId) {
        const action = async () => {
            const res = await this.axios.get(`news/${newsId}`)
            const arr = res.data ? [res.data] : []
            const mappedNews = arr.map(newsMapper)
            const filledNews = await this.#fillNewsParams(mappedNews)
            return filledNews[0]
        }

        return await Api.#runRequest(action)
    }

    async getCommentReports(id) {
        const action = async () => {
            const res = await this.axios.get(`comments/${id}/reports`)
            return res.data || []
        }

        return await Api.#runRequest(action)
    }

    #validateId(id) {
        if (!id) {
            this.enqueueSnackbar("User not logged in")
            throw new Error("User not logged in")
        }
    }

    async getArticleReports(id) {
        const action = async () => {
            const res = await this.axios.get(`news/${id}/reports`)
            return res.data || []
        }

        return await Api.#runRequest(action)
    }

    async getArticleReports(id) {
        const action = async () => {
            const res = await this.axios.get(`news/${id}/reports`)
            return res.data || []
        }

        return await Api.#runRequest(action)
    }

    async editUser(userDetails, image) {
        const action = async () => {
            const id = this.loggedUser.id
            this.#validateId(id)
            const res = await this.axios.put(`users/${id}`, JSON.stringify(userDetails),{
                headers: {
                    'Content-Type': 'application/json',
                }
            })
            if (image) {
                const f = new FormData()
                f.set('image', image)
                const fileRes = await this.axios.put(`users/${id}/image`, f)
            }
        }

        return await Api.#runRequest(action)
    }
}

