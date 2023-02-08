import {useAppContext} from "../context";
import PropTypes from 'prop-types';
import {getResourcePath} from "../constants";

export default function PositivityIndicator(props) {
    const ctx = useAppContext()
    const tooltipText = `${ctx.I18n("home.upvotes", [parseInt(props.upvoted*100)])} - ${ctx.I18n("home.interactions", [props.interactions])}`
    const imgMap = {
        positive: "/img/looking-positivity.svg",
        controversial: "/img/controversial-positivity.svg",
        negative: "/img/angry-positivity.svg"
    }





    return <img src={getResourcePath(imgMap[props.positivity || 'positive'])} alt="..."
                className={`quality-indicator${props.showNews ? '-news-view' : ''}`} data-toggle="tooltip" data-placement="top" title={tooltipText}
                data-original-title={tooltipText} />
}

PositivityIndicator.propTypes = {
    positivity: PropTypes.string,
    upvoted: PropTypes.number,
    interactions: PropTypes.number.isRequired,
}