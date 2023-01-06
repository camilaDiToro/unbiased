import PropTypes from "prop-types";

export default function ProfilePic(props) {
    const tier = props.tier

    return <img id={`${tier}-frame-color`} src="/img/profile-image.png"
                className="rounded-circle object-fit-cover img-div" width="80"/>
}

ProfilePic.propTypes = {
    tier: PropTypes.string.isRequired
}

