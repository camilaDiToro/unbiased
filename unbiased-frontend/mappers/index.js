import PropTypes from "prop-types";


export const newsMapper = (n) => {
return {
    ...n
}
}

export const userMapper = (u) => {
    return {
        nameOrEmail: u.username || u.email,
        hasImage: u.hasImage,
        id: u.id,
        tier: u.tier,
        isJournalist: u.journalist,
        stats: u.stats,
        image: u.image,
        description: u.description,
        mailOptions: u.mailOptions,
        hasPositivity: u.hasPositivity,
        username: u.username,
        email: u.email,
        followers: u.followers,
        following: u.following,
        newsStatistics: u.newsStats.map(s => ({
            progress: s.proportion,
            title: s['category'],
            i18n: true
        }))
    }
}