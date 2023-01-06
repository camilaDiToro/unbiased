import {useAppContext} from "../context";


export default function PositivityIndicator(props) {
    const ctx = useAppContext()
    const tooltipText = `${ctx.I18n("home.upvotes", [props.upvotes*100])}% - ${ctx.I18n("home.interactions", [props.intercations])}`
    const imgMap = {
        positive: "/img/looking-positivity.svg",
    }





    return <img src={imgMap[props.positivity || 'positive']} alt="..."
                className="quality-indicator" data-toggle="tooltip" data-placement="top" title={tooltipText}
                data-original-title={tooltipText} />
}