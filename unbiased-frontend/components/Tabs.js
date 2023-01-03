import { useRouter } from "next/router";
import Link from 'next/link';

function TabPills(className, items, selected, children) {
    const activeClasses = "bg-info active"
    const inactiveClasses = "text-secondary"
    return <>
        <ul className={`${className} mb-4 mt-4 nav bg-transparent nav-pills text-light p-2 rounded-lg d-flex `}>
            {items.map(item => <li className="nav-item" key={item.text}>
                <Link className={`text-capitalize nav-link fromLeft rounded-pill hover-pill ml-1 ${selected === item.text ? activeClasses : inactiveClasses}`}
                aria-current="page"
                   href={item.route}>
                    {item.text}
                </Link>
            </li>)}
            {children}
        </ul>
    </>
}

function TabTransparent(className, items, selected) {
    return<>
        <ul className="my-2 nav nav-tabs justify-content-center text-light p-2">
            {items.map(item => <li className="nav-item" key={item.text}>
                <Link className={`text-capitalize text-white nav-link tabs ${selected === item.text ? 'active' : ''}`}
                      aria-current="page" href={item.route}>
                    {item.text}
                </Link>
            </li>)}

        </ul>
    </>
}

export default function Tabs(props) {
    const router = useRouter();
    const {items, selected, children, className} = props

   if (props.pill) {
        return TabPills(className, items, selected, children)
    } else {
       return TabTransparent(className, items, selected, children)
    }
}