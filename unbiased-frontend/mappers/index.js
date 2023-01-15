import PropTypes from "prop-types";

export const userMapper = (u) => {
    return {
        nameOrEmail: u.username || u.email,
        hasImage: u.hasImage,
        id: u.id,
        tier: u.tier,
        isJournalist: u.isJournalist,
        stats: {
            positivity: u.positivity,
            upvoted: u.upvotedProportion,
            interactions: u.interactions},
        image: u.image
    }
}