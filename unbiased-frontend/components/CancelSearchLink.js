import Link from "next/link";

export default function CancelSearchLink(props) {
    return <div className="m-3 ">
        <Link className="link" href="/">
            <div className="link-text">
                {props.text}
            </div>
        </Link>
    </div>
}